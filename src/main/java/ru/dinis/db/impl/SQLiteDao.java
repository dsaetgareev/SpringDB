package ru.dinis.db.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.dinis.db.interfaces.MP3Dao;
import ru.dinis.db.objects.MP3;

import javax.sql.DataSource;
import java.util.List;

/**
 * Create by dinis of 07.01.18.
 */
@Component("sqliteDao")
public class SQLiteDao implements MP3Dao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(MP3 mp3) {
        String sql = "insert into mp3 (name, author) values(?, ?)";

        this.jdbcTemplate.update(sql, new Object[]{mp3.getName(), mp3.getAuthor()});
    }

    @Override
    public void insert(List<MP3> mp3List) {
        for (MP3 mp3 : mp3List) {
            this.insert(mp3);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "delete from mp3 where id=?";
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public void delete(MP3 mp3) {
        this.delete(mp3.getId());
    }

    @Override
    public MP3 getMP3ById(int id) {
        return null;
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        return null;
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        return null;
    }
}
