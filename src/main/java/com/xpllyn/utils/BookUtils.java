package com.xpllyn.utils;

import com.xpllyn.pojo.Book;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookUtils {

    /**
     * 获取书籍列表
     */
    public List getFileName(String path) throws FileNotFoundException {
        List<Book> books = new ArrayList<>();
        File file = ResourceUtils.getFile(path);
        String[] names = file.list();
        for(int i = 0; i < names.length; i++){
            Book b = new Book();
            b.setName(names[i]);
            books.add(b);
        }
        return books;
    }

//    public static void main(String[] args) {
//        String path = "E://book";
//        String[] names = getFileName(path);
//        for(String name : names){
//            System.out.println(name);
//        }
//    }
}
