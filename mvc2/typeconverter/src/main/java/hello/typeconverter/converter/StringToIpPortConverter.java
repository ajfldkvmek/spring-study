package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import hello.typeconverter.type.IpPort;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {
        log.info("convert source: {}", source);
        // 127.0.0.1:8080 으로 들어올 것을 기대
        String[] split = source.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        log.info("ip: {}, port: {}", ip, port);
        return new IpPort(ip, port);
    }


}
