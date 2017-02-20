package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public interface ClientCommand {

    void execute(Client client);

    boolean checkArguments();

    List<String> getArguments();

}
