package com.reliaquest.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ApiApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    void someTest() {
        // do the thing here
    	assertThat(applicationContext.containsBean("employeeService")).isTrue();
    }
}
