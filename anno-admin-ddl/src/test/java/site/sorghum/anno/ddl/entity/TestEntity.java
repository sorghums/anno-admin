package site.sorghum.anno.ddl.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author songyinyin
 * @since 2023/7/4 11:51
 */
@Data
public class TestEntity {

  protected String id;

  private String createBy;

  private LocalDateTime createTime;

  private BigDecimal bigDecimalNum;

  private  Float floatNum;

  private Double doubleNum;

  private Integer integerNum;

  private Long longNum;

  private Date utilDate;

  private java.sql.Date sqlDate;


}
