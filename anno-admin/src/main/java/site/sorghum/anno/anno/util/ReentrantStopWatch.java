package site.sorghum.anno.anno.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 可重入，第二次之后 start 的任务，默认为子任务
 *
 * @author songyinyin
 * @since 2023/10/18 18:11
 */
public class ReentrantStopWatch {

    /**
     * 创建计时任务（秒表）
     *
     * @param id 用于标识秒表的唯一ID
     * @return StopWatch
     * @since 5.5.2
     */
    public static ReentrantStopWatch create(String id) {
        return new ReentrantStopWatch(id);
    }

    /**
     * 秒表唯一标识，用于多个秒表对象的区分
     */
    private final String id;
    private List<TaskInfo> taskList;

    private Stack<TaskInfo> taskStack;

    /**
     * 最后一次任务对象
     */
    private TaskInfo lastTaskInfo;
    /**
     * 总任务数
     */
    private int taskCount;
    /**
     * 总运行时间
     */
    private long totalTimeNanos;
    // ------------------------------------------------------------------------------------------- Constructor start

    /**
     * 构造，不启动任何任务
     */
    public ReentrantStopWatch() {
        this(StrUtil.EMPTY);
    }

    /**
     * 构造，不启动任何任务
     *
     * @param id 用于标识秒表的唯一ID
     */
    public ReentrantStopWatch(String id) {
        this(id, true);
    }

    /**
     * 构造，不启动任何任务
     *
     * @param id           用于标识秒表的唯一ID
     * @param keepTaskList 是否在停止后保留任务，{@code false} 表示停止运行后不保留任务
     */
    public ReentrantStopWatch(String id, boolean keepTaskList) {
        this.id = id;
        if (keepTaskList) {
            this.taskList = new ArrayList<>();
            this.taskStack = new Stack<>();
        }
    }
    // ------------------------------------------------------------------------------------------- Constructor end

    /**
     * 获取StopWatch 的ID，用于多个秒表对象的区分
     *
     * @return the ID 默认为空字符串
     * @see #ReentrantStopWatch(String)
     */
    public String getId() {
        return this.id;
    }

    /**
     * 设置是否在停止后保留任务，{@code false} 表示停止运行后不保留任务
     *
     * @param keepTaskList 是否在停止后保留任务
     */
    public void setKeepTaskList(boolean keepTaskList) {
        if (keepTaskList) {
            if (null == this.taskList) {
                this.taskList = new ArrayList<>();
            }
        } else {
            this.taskList = null;
        }
    }

    /**
     * 开始默认的新任务
     *
     * @throws IllegalStateException 前一个任务没有结束
     */
    public void start() throws IllegalStateException {
        start(StrUtil.EMPTY);
    }

    /**
     * 开始指定名称的新任务
     *
     * @param taskName 新开始的任务名称
     * @throws IllegalStateException 前一个任务没有结束
     */
    public void start(String taskName) throws IllegalStateException {
        TaskInfo taskInfo;
        if (taskStack.empty()) {
            taskInfo = new TaskInfo(taskName);
        } else {
            taskInfo = new TaskInfo("-".repeat(taskStack.size()) + "> " + taskName);
        }
        taskInfo.setStartTimeNanos(System.nanoTime());
        this.lastTaskInfo = taskInfo;
        if (null != this.taskList) {
            this.taskStack.push(taskInfo);
            this.taskList.add(taskInfo);
        }
    }

    /**
     * 停止当前任务
     *
     * @throws IllegalStateException 任务没有开始
     */
    public void stop() throws IllegalStateException {
        if (taskStack.empty()) {
            throw new IllegalStateException("Can't stop StopWatch %s: it's not running".formatted(this.id));
        }
        TaskInfo taskInfo = taskStack.pop();
        final long lastTime = System.nanoTime() - taskInfo.getStartTimeNanos();
        this.totalTimeNanos += lastTime;
        taskInfo.setTimeNanos(lastTime);
        ++this.taskCount;

    }

    /**
     * 获取当前任务名，{@code null} 表示无任务
     *
     * @return 当前任务名，{@code null} 表示无任务
     */
    public String currentTaskName() {
        return this.lastTaskInfo.getTaskName();
    }

    /**
     * 获取最后任务的花费时间（纳秒）
     *
     * @return 任务的花费时间（纳秒）
     * @throws IllegalStateException 无任务
     */
    public long getLastTaskTimeNanos() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return this.lastTaskInfo.getTimeNanos();
    }

    /**
     * 获取最后任务的花费时间（毫秒）
     *
     * @return 任务的花费时间（毫秒）
     * @throws IllegalStateException 无任务
     */
    public long getLastTaskTimeMillis() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return this.lastTaskInfo.getTimeMillis();
    }

    /**
     * 获取最后的任务名
     *
     * @return 任务名
     * @throws IllegalStateException 无任务
     */
    public String getLastTaskName() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task name");
        }
        return this.lastTaskInfo.getTaskName();
    }

    /**
     * 获取最后的任务对象
     *
     * @return {@link TaskInfo} 任务对象，包括任务名和花费时间
     * @throws IllegalStateException 无任务
     */
    public TaskInfo getLastTaskInfo() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task info");
        }
        return this.lastTaskInfo;
    }

    /**
     * 获取所有任务的总花费时间
     *
     * @param unit 时间单位，{@code null}表示默认{@link TimeUnit#NANOSECONDS}
     * @return 花费时间
     * @since 5.7.16
     */
    public long getTotal(TimeUnit unit) {
        return unit.convert(this.totalTimeNanos, TimeUnit.NANOSECONDS);
    }

    /**
     * 获取所有任务的总花费时间（纳秒）
     *
     * @return 所有任务的总花费时间（纳秒）
     * @see #getTotalTimeMillis()
     * @see #getTotalTimeSeconds()
     */
    public long getTotalTimeNanos() {
        return this.totalTimeNanos;
    }

    /**
     * 获取所有任务的总花费时间（毫秒）
     *
     * @return 所有任务的总花费时间（毫秒）
     * @see #getTotalTimeNanos()
     * @see #getTotalTimeSeconds()
     */
    public long getTotalTimeMillis() {
        return getTotal(TimeUnit.MILLISECONDS);
    }

    /**
     * 获取所有任务的总花费时间（秒）
     *
     * @return 所有任务的总花费时间（秒）
     * @see #getTotalTimeNanos()
     * @see #getTotalTimeMillis()
     */
    public double getTotalTimeSeconds() {
        return DateUtil.nanosToSeconds(this.totalTimeNanos);
    }

    /**
     * 获取任务数
     *
     * @return 任务数
     */
    public int getTaskCount() {
        return this.taskCount;
    }

    /**
     * 获取任务列表
     *
     * @return 任务列表
     */
    public TaskInfo[] getTaskInfo() {
        if (null == this.taskList) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        }
        return this.taskList.toArray(new TaskInfo[0]);
    }

    /**
     * 获取任务信息，类似于：
     * <pre>
     *     StopWatch '[id]': running time = [total] ns
     * </pre>
     *
     * @return 任务信息
     */
    public String shortSummary() {
        return shortSummary(null);
    }

    /**
     * 获取任务信息，类似于：
     * <pre>
     *     StopWatch '[id]': running time = [total] [unit]
     * </pre>
     *
     * @param unit 时间单位，{@code null}则默认为{@link TimeUnit#NANOSECONDS}
     * @return 任务信息
     */
    public String shortSummary(TimeUnit unit) {
        if (null == unit) {
            unit = TimeUnit.NANOSECONDS;
        }
        return StrUtil.format("StopWatch '{}': running time = {} {}",
            this.id, getTotal(unit), DateUtil.getShotName(unit));
    }

    /**
     * 生成所有任务的一个任务花费时间表，单位纳秒
     *
     * @return 任务时间表
     */
    public String prettyPrint() {
        return prettyPrint(null, null);
    }

    /**
     * 生成所有任务的一个任务花费时间表，单位纳秒
     *
     * @return 任务时间表
     */
    public String prettyPrint(TimeUnit unit) {
        return prettyPrint(unit, null);
    }

    /**
     * 生成所有任务的一个任务花费时间表
     *
     * @param unit 时间单位，{@code null}则默认{@link TimeUnit#NANOSECONDS} 纳秒
     * @param filter 过滤器，用于过滤不需要打印的任务
     * @return 任务时间表
     * @since 5.7.16
     */
    public String prettyPrint(TimeUnit unit, Predicate<TaskInfo> filter) {
        if (null == unit) {
            unit = TimeUnit.NANOSECONDS;
        }

        final StringBuilder sb = new StringBuilder(shortSummary(unit));
        sb.append(FileUtil.getLineSeparator());
        if (null == this.taskList) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());
            sb.append(DateUtil.getShotName(unit)).append("         %     Task name").append(FileUtil.getLineSeparator());
            sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());

            final NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(9);
            nf.setGroupingUsed(false);

            final NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(2);
            pf.setGroupingUsed(false);

            for (TaskInfo task : getTaskInfo()) {
                if (filter != null && filter.test(task)) {
                    continue;
                }
                sb.append(nf.format(task.getTime(unit))).append("  ");
                sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("   ");
                sb.append(task.getTaskName()).append(FileUtil.getLineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(shortSummary());
        if (null != this.taskList) {
            for (TaskInfo task : this.taskList) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
                long percent = Math.round(100.0 * task.getTimeNanos() / getTotalTimeNanos());
                sb.append(" = ").append(percent).append("%");
            }
        } else {
            sb.append("; no task info kept");
        }
        return sb.toString();
    }

    /**
     * 存放任务名称和花费时间
     *
     * @author Looly
     */
    public static final class TaskInfo {

        /**
         * 任务名称
         */
        @Getter
        private final String taskName;

        /**
         * 花费时间（纳秒）
         */
        @Getter
        @Setter
        private long timeNanos;

        /**
         * 子任务开始时间（纳秒）
         */
        @Getter
        @Setter
        private long startTimeNanos;

        /**
         * 构造
         *
         * @param taskName 任务名称
         */
        TaskInfo(String taskName) {
            this.taskName = taskName;
        }

        /**
         * 获取指定单位的任务花费时间
         *
         * @param unit 单位
         * @return 任务花费时间
         * @since 5.7.16
         */
        public long getTime(TimeUnit unit) {
            return unit.convert(this.timeNanos, TimeUnit.NANOSECONDS);
        }

        /**
         * 获取任务花费时间（单位：毫秒）
         *
         * @return 任务花费时间（单位：毫秒）
         * @see #getTimeNanos()
         * @see #getTimeSeconds()
         */
        public long getTimeMillis() {
            return getTime(TimeUnit.MILLISECONDS);
        }

        /**
         * 获取任务花费时间（单位：秒）
         *
         * @return 任务花费时间（单位：秒）
         * @see #getTimeMillis()
         * @see #getTimeNanos()
         */
        public double getTimeSeconds() {
            return DateUtil.nanosToSeconds(this.timeNanos);
        }
    }
}
