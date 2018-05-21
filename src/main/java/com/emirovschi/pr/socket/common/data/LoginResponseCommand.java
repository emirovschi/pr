package com.emirovschi.pr.socket.common.data;

import java.util.Map;

public class LoginResponseCommand extends AbstractCommand
{
    private static final String SUCCESS = "success";
    private static final String TRUE = "1";
    private static final String FALSE = "0";

    public LoginResponseCommand(final boolean success)
    {
        super(SUCCESS, success ? TRUE : FALSE);
    }

    public LoginResponseCommand(final Map<String, String> data)
    {
        super(data);
    }

    public boolean getSuccess()
    {
        return TRUE.equals(getData().get(SUCCESS));
    }

    public void setSuccess(final boolean success)
    {
        getData().put(SUCCESS, success ? TRUE : FALSE);
    }
}
