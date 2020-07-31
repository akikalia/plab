//package interceptor;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//public class AuthInterceptor extends HandlerInterceptorAdapter {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
////        if (request.getSession() == null){
////            response.sendRedirect("/");
////            return false;
////        }
////        Object user = request.getSession().getAttribute("user");
////        if (user == null){
////            response.sendRedirect("/");
////            return false;
////        }
//        return true;
//    }
//
//}
