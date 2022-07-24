package bg.softuni.fitcom.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class ViewsInterceptor implements HandlerInterceptor {

    private final Map<Long, Integer> trainingProgramIdsToViews = new HashMap<>();
    private final Map<Long, Integer> dietIdsToViews = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        long delimiterCount = requestURI.chars().filter(ch -> ch == '/').count();

        if (requestURI.contains("/training-programs/")
                && !requestURI.contains("/add")
                && delimiterCount == 2) {
            String index = requestURI.substring(requestURI.lastIndexOf("/") + 1);

            long indexParsed = Long.parseLong(index);
            trainingProgramIdsToViews.putIfAbsent(indexParsed, 0);
            trainingProgramIdsToViews.put(indexParsed, trainingProgramIdsToViews.get(indexParsed) + 1);
        } else if (requestURI.contains("/diets/")
                && !requestURI.contains("/add")
                && delimiterCount == 2) {
            String index = requestURI.substring(requestURI.lastIndexOf("/") + 1);

            long indexParsed = Long.parseLong(index);
            dietIdsToViews.putIfAbsent(indexParsed, 0);
            dietIdsToViews.put(indexParsed, dietIdsToViews.get(indexParsed) + 1);
        }

        return true;
    }

    public Map<Long, Integer> getTrainingProgramIdsToViews() {
        return trainingProgramIdsToViews;
    }

    public Map<Long, Integer> getDietIdsToViews() {
        return dietIdsToViews;
    }
}
