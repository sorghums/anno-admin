package site.sorghum.anno.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.warm.flow.core.entity.HisTask;
import org.dromara.warm.flow.core.entity.Task;
import org.noear.solon.annotation.Component;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.wood.IPage;
import org.noear.wood.SelectQ;
import org.noear.wood.annotation.Db;
import site.sorghum.anno.ao.CopyHisTaskAo;
import site.sorghum.anno.ao.DoneHisTaskAo;
import site.sorghum.anno.ao.WaitFlowTaskAo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AnnoFlowService {

    @Db
    DbContext dbContext;

    @SneakyThrows
    public IPage<WaitFlowTaskAo> toDoPage(Task task,
                                          int pageIndex, int pageSize) {
        DbTableQuery tableQuery = dbContext.table("flow_task As t")
            .leftJoin("flow_definition AS d").on("t.definition_id = d.id")
            .leftJoin("flow_instance AS i").on("t.instance_id = i.id")
            .where("t.node_type = 1");
        List<String> permissionList = task.getPermissionList();
        if (CollUtil.isNotEmpty(permissionList)) {
            String inStr = permissionList.stream().map(String::trim).map(
                "'%s'"::formatted
            ).collect(Collectors.joining(",", "(", ")"));
            tableQuery.and("""
                (select uu.processed_by
                       from `flow_user` AS uu
                       where uu.associated = t.id
                         AND uu.processed_by in ? limit 1) IS NOT NULL
                """, inStr);
        }
        if (StrUtil.isNotBlank(task.getNodeCode())) {
            tableQuery.andEq("u.node_code", task.getNodeCode());
        }
        if (StrUtil.isNotBlank(task.getNodeName())) {
            tableQuery.andLk("t.node_name", "%" + task.getNodeName() + "%");
        }
        if (Objects.nonNull(task.getInstanceId())) {
            tableQuery.andEq("t.instance_id", task.getInstanceId());
        }
        if (Objects.nonNull(task.getTenantId())) {
            tableQuery.andEq("t.tenant_id", task.getTenantId());
        }
        tableQuery.orderByDesc("t.create_time");
        int start = (pageIndex - 1) * pageSize;
        return tableQuery.paging(start, pageSize).selectPage("""
                       distinct t.id,
                       t.node_code,
                       t.node_name,
                       t.node_type,
                       t.definition_id,
                       t.instance_id,
                       t.create_time,
                       t.update_time,
                       t.tenant_id,
                       i.business_id,
                       i.flow_status,
                       i.activity_status,
                       d.flow_name,
                       t.form_custom,
                       t.form_path
            """, WaitFlowTaskAo.class);
    }

    @SneakyThrows
    public IPage<DoneHisTaskAo> donePage(HisTask task,
                                         int pageIndex, int pageSize) {
        DbTableQuery tableQuery = dbContext.table("flow_his_task As ct")
            .where("1=1");
        List<String> permissionList = task.getPermissionList();
        if (CollUtil.isNotEmpty(permissionList)) {
            tableQuery.andIn("ct.approver", permissionList);
        }
        if (StrUtil.isNotBlank(task.getNodeCode())) {
            tableQuery.andEq("ctt.node_code", task.getNodeCode());
        }
        if (StrUtil.isNotBlank(task.getNodeName())) {
            tableQuery.andLk("ctt.node_name", "%" + task.getNodeName() + "%");
        }
        if (Objects.nonNull(task.getInstanceId())) {
            tableQuery.andEq("ctt.instance_id", task.getInstanceId());
        }
        tableQuery.groupBy("ct.instance_id");
        SelectQ selectQ = tableQuery.selectQ("MAX(id) as id");
        tableQuery = dbContext.table("#( %s ) AS tmp".formatted(selectQ))
            .leftJoin("flow_his_task AS t").on("t.id = tmp.id")
            .leftJoin("flow_definition AS d").on("t.definition_id = d.id")
            .leftJoin("flow_instance AS i").on("t.instance_id = i.id")
            .orderByDesc("t.create_time");
        int start = (pageIndex - 1) * pageSize;
        return tableQuery.paging(start, pageSize).selectPage("""
                        t.id,
                        t.node_code,
                        t.node_name,
                        t.cooperate_type,
                        t.approver,
                        t.collaborator,
                        t.node_type,
                        t.target_node_code,
                        t.target_node_name,
                        t.definition_id,
                        t.instance_id,
                        i.flow_status,
                        t.message,
                        t.ext,
                        t.create_time,
                        t.update_time,
                        t.tenant_id,
                        i.business_id,
                        t.form_path,
                        d.flow_name
            """, DoneHisTaskAo.class);
    }

    @SneakyThrows
    public IPage<CopyHisTaskAo> copyPage(HisTask task,
                                         int pageIndex, int pageSize) {
        DbTableQuery tableQuery = dbContext.table("flow_user AS a").leftJoin("flow_instance AS b").on("a.associated = b.id")
            .leftJoin("an_user AS c").on("b.create_by = c.id")
            .leftJoin("flow_definition AS d").on("b.definition_id = d.id")
            .where("a.type = 4");
        if (StrUtil.isNotBlank(task.getFlowName())) {
            tableQuery.andLk("c.name", "%" + task.getFlowName() + "%");
        }
        if (StrUtil.isNotBlank(task.getNodeName())) {
            tableQuery.andLk("c.node_name", "%" + task.getNodeName() + "%");
        }
        if (Objects.nonNull(task.getNodeType())) {
            tableQuery.andEq("b.node_type", task.getNodeType());
        }
        tableQuery.orderByDesc("a.create_time");
        int start = (pageIndex - 1) * pageSize;
        return tableQuery.paging(start, pageSize).selectPage("""
                        b.flow_status,
                        b.business_id,
                        a.create_time,
                        b.node_name,
                        b.id ,
                        d.flow_name
            """, CopyHisTaskAo.class);
    }
}
