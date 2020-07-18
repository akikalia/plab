package controller;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener()
public class plabSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute("user", null);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        event.getSession().setAttribute("user", null);
    }
}
