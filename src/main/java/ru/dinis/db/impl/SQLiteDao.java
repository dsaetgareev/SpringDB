package ru.dinis.db.impl;


import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.dinis.db.interfaces.MP3Dao;
import ru.dinis.db.objects.Author;
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

    private static final String MP3TABLE = "mp3";
    private static final String MP3_VIEW = "mp3_view";

    private NamedParameterJdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertMP3;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertMP3 = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name", "author");

    }

    @Override
    public void insert(MP3 mp3) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        String sqlInsertMp3 = "INSERT INTO mp3 (name, author_id) VALUES(:name, :author_id)";
        int author_id = insertAuthor(mp3.getAuthor());
        params.addValue("name", mp3.getName());
        params.addValue("author_id", author_id);
        this.jdbcTemplate.update(sqlInsertMp3, params);

    }
    @Override
    public int insertAuthor(Author author) {
        String sql = "INSERT INTO author (name) VALUES(:name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", author.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void insert(List<MP3> mp3List) {
        for (MP3 mp3 : mp3List) {
            this.insert(mp3);
        }
//        String sql = "INSERT INTO mp3 (name, author) VALUES(:name, :author)";
//        System.out.println(this.jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(mp3List)).length);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM mp3 WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(MP3 mp3) {
        this.delete(mp3.getId());
    }

    @Override
    public void deleteByAuthor(String author) {
        String sql = "DELETE FROM mp3 WHERE author=:author";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", author);
        System.out.println(this.jdbcTemplate.update(sql, params));
    }

    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM mp3 WHERE name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public MP3 getMP3ById(int id) {
        String sql = "SELECT * FROM " + MP3_VIEW + " WHERE mp3_id=:mp3_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_id", id);
        return this.jdbcTemplate.queryForObject(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        String sql = "SELECT * FROM mp3_view WHERE upper(mp3_name) LIKE :mp3_name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_name", "%" + name.toUpperCase() + "%");
        return this.jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        String sql = "SELECT * FROM mp3_view WHERE upper(author_name) LIKE :author_name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author_name", "%" + author.toUpperCase() + "%");
        return this.jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public int getMP3Count() {
        String sql = "SELECT count(*) FROM mp3";
        return this.jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    @Override
    public void allShow() {
        String sql = "SELECT * FROM mp3_view";
        List<MP3> list = this.jdbcTemplate.query(sql, new MP3RowMapper());
        for (MP3 mp3 : list) {
            System.out.println(mp3);
        }
    }

    @Override
    public void updateById(List<MP3> list) {
        String sql = "UPDATE mp3 SET name=:name, author=:author WHERE id=:id";
        this.jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(list));
    }

    public void showList(List<MP3> list) {
        for (MP3 mp3 : list) {
            System.out.println(mp3);
        }
    }

    public Map<String, Integer> getStat() {
        String sql = "SELECT author_name, count(*) as count FROM mp3_view group by author_name";
        final Map<String, Integer> map = new TreeMap<String, Integer>();

        return this.jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Integer>>() {
            @Nullable
            @Override
            public Map<String, Integer> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                while (resultSet.next()) {
                    String author = resultSet.getString("author_name");
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
            Author author = new Author();
            author.setId(resultSet.getInt("author_id"));
            author.setName(resultSet.getString("author_name"));

            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("mp3_id"));
            mp3.setName(resultSet.getString("mp3_name"));
            mp3.setAuthor(author);
            return mp3;
        }
    }
}
