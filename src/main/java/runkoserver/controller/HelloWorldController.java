package runkoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HelloWorldController {
    
    @RequestMapping("/hello")
    public String Hello() {
        return "Hello World!\n"
                + "             ,   ,   ,   ,             \n" +
"           , |_,_|_,_|_,_| ,           \n" +
"       _,-=|;  |,  |,  |,  |;=-_       \n" +
"     .-_| , | , | , | , | , |  _-.     \n" +
"     |:  -|:._|___|___|__.|:=-  :|     \n" +
"     ||*:  :    .     .    :  |*||     \n" +
"     || |  | *  |  *  |  * |  | ||     \n" +
" _.-=|:*|  |    |     |    |  |*:|=-._ \n" +
"-    `._:  | *  |  *  |  * |  :_.'    -\n" +
" =_      -=:.___:_____|___.: =-     _= \n" +
"    - . _ __ ___  ___  ___ __ _ . -    ";
    }
}
