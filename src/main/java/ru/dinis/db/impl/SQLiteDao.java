package ru.dinis.db.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.dinis.db.interfaces.MP3Dao;
import ru.dinis.db.objects.MP3;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Create by dinis of 07.01.18.
 */
@Component("sqliteDao")
public class SQLiteDao implements MP3Dao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void insert(MP3 mp3) {
        String sql = "insert into mp3 (name, author) values(:name, :author)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", mp3.getName());
        params.addValue("author", mp3.getAuthor());

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void insert(List<MP3> mp3List) {
        for (MP3 mp3 : mp3List) {
            this.insert(mp3);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "delete from mp3 where id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(MP3 mp3) {
        this.delete(mp3.getId());
    }

    @Override
    public MP3 getMP3ById(int id) {
        String sql = "SELECT * FROM mp3 WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return this.jdbcTemplate.queryForObject(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        String sql = "SELECT * FROM mp3 WHERE upper(name) like :name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", "%" + name.toUpperCase() + "%");
        return this.jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        String sql = "SELECT * FROM mp3 WHERE upper(author) like :author";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", "%" + author.toUpperCase() + "%");
        return this.jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public int getMP3Count() {
        String sql = "SELECT count(*) FROM mp3";
        return this.jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    public void allShow() {
        String sql = "SELECT * FROM mp3";
        List<MP3> list = this.jdbcTemplate.query(sql, new MP3RowMapper());
        for (MP3 mp3 : list) {
            System.out.println(mp3);
        }
    }

    public void showList(List<MP3> list) {
        for (MP3 mp3 : list) {
            System.out.println(mp3);
        }
    }

    public Map<String, Integer> getStat() {
        String sql = "SELECT author, count(*) as count FROM mp3 group by author";
        final Map<String, Integer> map = new TreeMap<String, Integer>();

        return this.jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Integer>>() {
            @Nullable
            @Override
            public Map<String, Integer> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                while (resultSet.next()) {
                    String author = resultSet.getString("author");
                    Integer count = resultSet.getInt("count");
                    map.put(author, count);
                }
                return map;
            }
        });
    }

    public static final class MP3RowMapper implements RowMapper<MP3> {
        @Nullable
        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setName(resultSet.getString("name"));
            mp3.setAuthor(resultSet.getString("author"));
            return mp3;
        }
    }
}
