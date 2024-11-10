package telran.appl.net;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class ReverseLengthProtocol implements Protocol {

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response = null;
        try {
            response = switch (type) {
                case "reverse" -> reverse(data);
                case "length" -> length(data);
                default -> new Response(ResponseCode.WRONG_TYPE, type + " is wrong type");
            };
        } catch (Exception e) {
           response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }
        return response;
    }

    Response reverse(String data) {
        if(data.length() < 2) {
            throw new IllegalArgumentException("too few string length: must be greater than 1");
        }
        String res = new StringBuilder(data).reverse().toString();
        return getOkResponse(res);
    }
    Response length(String data) {
        String res = data.length() + "";
        return getOkResponse(res);
    }

    private Response getOkResponse(String res) {
       return new Response(ResponseCode.OK, res);
    }

}