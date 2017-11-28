package xyz.fz.springBootVertx.service.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import xyz.fz.springBootVertx.service.AbcService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class AbcServiceImpl implements AbcService {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public String hello(String name) {
        return "hello: " + name;
    }

    @Override
    public String record(String no) {
        String sql = "insert into t_test(no) values(:no) ";
        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        namedParameterJdbcTemplate.update(sql, params);
        return no;
    }
}
