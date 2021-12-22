package telran.net;

import telran.net.dto.RequestJava;
import telran.net.dto.ResponseJava;

public interface ApplProtocolJava {
    ResponseJava getResponse(RequestJava request);
}
