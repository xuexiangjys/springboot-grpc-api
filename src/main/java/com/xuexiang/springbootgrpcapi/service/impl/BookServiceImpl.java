package com.xuexiang.springbootgrpcapi.service.impl;

import com.github.pagehelper.PageHelper;
import com.xuexiang.springbootgrpcapi.mapper.BookMapper;
import com.xuexiang.springbootgrpcapi.model.Book;
import com.xuexiang.springbootgrpcapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/7/16 上午11:19
 */
@Service(value = "bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;//这里会报错，但是并不会影响

    @Override
    public boolean addBook(Book book) {
        return bookMapper.insert(book) > 0;
    }

    @Override
    public boolean deleteBook(int bookId) {
        return bookMapper.deleteByPrimaryKey(bookId) > 0;
    }

    @Override
    public boolean updateBook(Book record) {
        return bookMapper.updateByPrimaryKey(record) > 0;
    }

    @Override
    public boolean updatePictureByBookId(Book record) {
        return bookMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public Book findBookById(int bookId) {
        return bookMapper.selectByPrimaryKey(bookId);
    }

    @Override
    public List<Book> findAllBook(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return bookMapper.selectAll();
    }

    @Override
    public List<Book> findAllBook() {
        return bookMapper.selectAll();
    }
}
