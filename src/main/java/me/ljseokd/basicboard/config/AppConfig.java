package me.ljseokd.basicboard.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.convention.NameTokenizers.UNDERSCORE;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setDestinationNameTokenizer(UNDERSCORE)
                .setSourceNameTokenizer(UNDERSCORE);
        return modelMapper;
    }
}