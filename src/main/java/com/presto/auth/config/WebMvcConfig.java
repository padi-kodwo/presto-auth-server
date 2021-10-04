package com.presto.auth.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createGsonHttpMessageConverter());
//        super.addDefaultHttpMessageConverters(converters);
    }

    @Bean
    public GsonHttpMessageConverter createGsonHttpMessageConverter() {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(createGson());

        return gsonConverter;
    }

    @Bean
    public Gson createGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<DocsFormatterFilter> loggingFilter() {
        FilterRegistrationBean<DocsFormatterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DocsFormatterFilter());
        registrationBean.addUrlPatterns("/api_doc");
        return registrationBean;
    }


    private static class DocsFormatterFilter implements Filter {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            ByteResponseWrapper byteResponseWrapper = new ByteResponseWrapper((HttpServletResponse) servletResponse);
            ByteRequestWrapper byteRequestWrapper = new ByteRequestWrapper((HttpServletRequest) servletRequest);

            filterChain.doFilter(byteRequestWrapper, byteResponseWrapper);

            String jsonResponse = new String(byteResponseWrapper.getBytes(), servletResponse.getCharacterEncoding());
            String result = StringUtils.hasText(jsonResponse) ? jsonResponse
                    .substring(1, jsonResponse.length() - 1)
                    .replaceAll("\\\\\"", "\"") : jsonResponse;
            servletResponse.getOutputStream().write(result.getBytes(servletResponse.getCharacterEncoding()));
        }

        static class ByteResponseWrapper extends HttpServletResponseWrapper {

            private final PrintWriter writer;
            private final ByteOutputStream output;

            public byte[] getBytes() {
                writer.flush();
                return output.getBytes();
            }

            public ByteResponseWrapper(HttpServletResponse response) {
                super(response);
                output = new ByteOutputStream();
                writer = new PrintWriter(output);
            }

            @Override
            public PrintWriter getWriter() {
                return writer;
            }

            @Override
            public ServletOutputStream getOutputStream() {
                return output;
            }
        }

        static class ByteRequestWrapper extends HttpServletRequestWrapper {

            byte[] requestBytes = null;
            private ByteInputStream byteInputStream;


            public ByteRequestWrapper(HttpServletRequest request) throws IOException {
                super(request);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                InputStream inputStream = request.getInputStream();

                byte[] buffer = new byte[4096];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, read);
                }

                replaceRequestPayload(byteArrayOutputStream.toByteArray());
            }

            @Override
            public BufferedReader getReader() {
                return new BufferedReader(new InputStreamReader(getInputStream()));
            }

            @Override
            public ServletInputStream getInputStream() {
                return byteInputStream;
            }

            public void replaceRequestPayload(byte[] newPayload) {
                requestBytes = newPayload;
                byteInputStream = new ByteInputStream(new ByteArrayInputStream(requestBytes));
            }
        }

        static class ByteOutputStream extends ServletOutputStream {

            private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

            @Override
            public void write(int b) {
                bos.write(b);
            }

            public byte[] getBytes() {
                return bos.toByteArray();
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }
        }

        static class ByteInputStream extends ServletInputStream {

            private final InputStream inputStream;

            public ByteInputStream(final InputStream inputStream) {
                this.inputStream = inputStream;
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        }

    }
}