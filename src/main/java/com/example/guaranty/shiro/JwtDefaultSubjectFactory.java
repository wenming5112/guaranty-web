package com.example.guaranty.shiro;


import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;


/**
 * Jwt config
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/6/16 17:47
 **/
public class JwtDefaultSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        // 不创建 session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}