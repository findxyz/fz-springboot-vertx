package xyz.fz.springBootVertx.service.impl;

import org.springframework.stereotype.Service;
import xyz.fz.springBootVertx.service.AbcService;

@Service
public class AbcServiceImpl implements AbcService {

    @Override
    public String hello(String name) {
        return "hello: " + name;
    }
}
