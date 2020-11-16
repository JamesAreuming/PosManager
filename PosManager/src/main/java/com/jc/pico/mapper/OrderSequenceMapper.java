package com.jc.pico.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jc.pico.bean.Sequence;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface OrderSequenceMapper {
	
//    @Select("SELECT SEQ_ID,SEQ_TODAY,SEQ_ORDER_NO,SEQ_RECEIPT_NO FROM tb_sequence")
//    public List<Sequence> selectSequenceList(Sequence sequenceData) throws Exception;
 
    //@Select("SELECT SEQ_ID,SEQ_TODAY,SEQ_ORDER_NO,SEQ_RECEIPT_NO FROM tb_sequence")
	//public List<Sequence> selectSequenceList() throws Exception;
    List<Sequence> selectByAll() throws Exception;
    
    Sequence selectByPrimaryKey(String seqId) throws Exception;
	
    //@Insert("INSERT INTO tb_sequence (SEQ_ID, SEQ_TODAY, SEQ_ORDER_NO, SEQ_RECEIPT) VALUES (#{seqId}, #{seqToday}, #{seqOrderNo}, #{seqReceiptNo})")
    //public int insertSequence(Sequence sequenceData) throws Exception;
    int insertSequence(Sequence sequenceData) throws Exception;
    
    //@Update("UPDATE tb_sequence SET SEQ_TODAY = #{seqToday}, SEQ_ORDER_NO = #{seqOrderNo}, SEQ_RECEIPT = #{seqReceiptNo} WHERE SEQ_ID = #{seqId}")
    //public int updateSequence(Sequence sequenceData) throws Exception;
    int updateSequence(Sequence sequenceData) throws Exception;

    //@Delete("DELETE FROM WHERE SEQ_ID = #{seqId}")
    //public int deleteSequence(Sequence sequenceData) throws Exception;
    int deleteSequence(Sequence sequenceData) throws Exception;

}

