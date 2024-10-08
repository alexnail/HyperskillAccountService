package account.repository;

import account.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class UserDAO implements InitializingBean {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByUserName(String username) {
        var sql = """
                SELECT *
                FROM users
                WHERE email = ?
                """;
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setName(rs.getString("firstname"));
                u.setLastname(rs.getString("lastname"));
                return u;
            });
            return Optional.ofNullable(user);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public User save(User entity) {
        String sql = """
                INSERT INTO users (email, password, firstname, lastname)
                VALUES (?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getName());
            ps.setString(4, entity.getLastname());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            entity.setId(keyHolder.getKey().longValue());
        }
        return entity;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                    email VARCHAR UNIQUE NOT NULL,
                    password VARCHAR NOT NULL,
                    firstname VARCHAR NOT NULL,
                    lastname VARCHAR NOT NULL
                )
                """;
        jdbcTemplate.update(sql);
    }
}
