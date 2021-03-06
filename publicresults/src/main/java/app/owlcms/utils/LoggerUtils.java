/*******************************************************************************
 * Copyright (c) 2009-2021 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("NPOSL-3.0")
 * License text at https://opensource.org/licenses/NPOSL-3.0
 *******************************************************************************/
package app.owlcms.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class LoggerUtils.
 */
public class LoggerUtils {

    /**
     * Where from.
     *
     * @return the string
     */
    public static String stackTrace() {
        StringWriter sw = new StringWriter();
        new Exception("").printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * @param t
     * @return
     */
    public static String stackTrace(Throwable t) {
        // IDEA: skip from "at javax.servlet.http.HttpServlet.service" to line starting with "Caused by"
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Where from.
     *
     * @return the string
     */
    public static String whereFrom() {
        return Thread.currentThread().getStackTrace()[3].toString();
    }
}
