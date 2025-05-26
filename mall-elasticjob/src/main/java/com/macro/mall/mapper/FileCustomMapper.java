package com.macro.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.macro.mall.domain.FileCustom;

@Mapper
public interface FileCustomMapper {

    @Select("select * from t_file_custom where backedUp=0")
    List<FileCustom> selectByZero();


    @Update("update t_file_custom set backedUp=1 where id=#{id}")
    void changeStatus(Long id);


    @Select("select * from t_file_custom where backedUp=0 and type = #{type}")
    List<FileCustom> selectByType(String type);


    @Select("select * from t_file_custom where backedUp=0 limit 0 , #{size}")
    List<FileCustom> fetchData(int size);
}