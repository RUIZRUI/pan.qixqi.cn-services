package cn.qixqi.pan.fs.controller;

import cn.qixqi.pan.fs.config.ServiceConfig;
import cn.qixqi.pan.fs.model.File;
import cn.qixqi.pan.fs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "filesystem")
public class FileController {

    @Autowired
    private ServiceConfig serviceConfig;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/example", method = RequestMethod.GET)
    public String example(){
        return serviceConfig.getExampleProperty();
    }

    @RequestMapping(value = "/file/{fileId}", method = RequestMethod.GET)
    public File getFileById(@PathVariable String fileId){
        return fileService.getFileById(fileId);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public List<File> getFiles(){
        return fileService.getFiles();
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public File addFile(@RequestBody File file){
        return fileService.addFile(file);
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public File updateFile(@RequestBody File file){
        return fileService.updateFile(file);
    }

    @RequestMapping(value = "/file/{fileId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteFile(@PathVariable String fileId){
        return fileService.deleteFile(fileId);
    }


}