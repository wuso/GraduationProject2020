package com.suda.scst.controller;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Major;
import com.suda.scst.domain.Student;
import com.suda.scst.services.MajorService;
import com.suda.scst.services.StudentService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

@RestController
//设置想要跳转的父路径
@RequestMapping("/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    //添加新专业
    @PostMapping(value = "/add")
    //接收post请求
    public void addMajor(@RequestBody Major major) {
        majorService.upsertMajor(major);
    }

    //删除专业
    @PostMapping(value = "/delete")
    public String deleteByName(@RequestParam(value = "name") String name) {
        majorService.deleteByName(name);
        return "delete ok";
    }

    //编辑班级
    @PostMapping(value = "/edit")
    public String editByName(@RequestBody Major origin) {
        //不可更改原节点的name的值
        Major major = majorService.findByName(origin.getName());
        //修改原节点的信息
        major.setFaculty(origin.getFaculty());
        major.setResearch(origin.getResearch());
        major.setMajor_id(origin.getMajor_id());
        //更新节点
        majorService.upsertMajor(major);
        return "edit ok";
    }

    //查询专业
    @GetMapping("/query")
    public Collection<Major> query(@RequestParam(value = "name") String name) {
        return majorService.findByNameLike(name);
    }

    //poi导入
    @RequestMapping("/importList")
    public void importList(@RequestParam(value = "file") MultipartFile mFile) throws IOException {
        assert mFile != null;
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(mFile.getInputStream()));
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            double id = row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String faculty = row.getCell(2).getStringCellValue();
            String research = row.getCell(3).getStringCellValue();
            Major user = new Major();
            user.setMajor_id((int) id);
            user.setName(name);
            user.setFaculty(faculty);
            user.setResearch(research);
            //查看数据库中是否有名字相同的专业，有则更新该节点，否则新建节点
            if(majorService.findByName(name)!=null){
                editByName(user);
            }
            else{
                addMajor(user);
            }
//            将对象添加数据库
            System.out.println(user.getName());
        }
    }

    //poi导出
    @RequestMapping("/exportList")
    public void exportList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "测试"+System.currentTimeMillis()+".xls";
        String[] titles = {"编号","名称","所属院系","所属门类"};
        List<Major> result = (List<Major>)  majorService.findByNameLike("**");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("majors");
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) (20 * 30));
        for (int i = 0; i < titles.length; i++) {
            //创建单元格
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        HSSFRow row = sheet.createRow(1);
        for (int i = 0; i < result.size(); i++) {
            row = sheet.createRow(i + 1);
            Major aMajor = result.get(i);
            row.createCell(0).setCellValue(aMajor.getMajor_id());
            row.createCell(1).setCellValue(aMajor.getName());
            row.createCell(2).setCellValue(aMajor.getFaculty());
            row.createCell(3).setCellValue(aMajor.getResearch());
            row.setHeight((short) (20 * 30));
        }
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
        workbook.write(os);
        os.flush();
        os.close();
    }

}
