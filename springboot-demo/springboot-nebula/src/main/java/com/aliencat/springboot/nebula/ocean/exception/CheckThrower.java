
package com.aliencat.springboot.nebula.ocean.exception;

import com.aliencat.springboot.nebula.ocean.common.ResponseService;
import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;

/**
 * Description  CheckThrower is used for
 *
 * @author Anyzm
 * Date  2021/7/15 - 15:07
 * @version 1.0.0
 */
public class CheckThrower {

    public static void ifTrueThrow(boolean flag, String exceptionDesc) {
        if (flag) {
            throw new RuntimeException(exceptionDesc);
        }
    }

    public static void ifFalseThrow(boolean flag, String exceptionDesc) {
        if (!flag) {
            throw new RuntimeException(exceptionDesc);
        }
    }

    public static void ifTrueThrow(boolean flag, ErrorEnum responseService) {
        if (flag) {
            throw new NebulaException(responseService);
        }
    }

    public static void ifFalseThrow(boolean flag, ResponseService responseService) {
        if (!flag) {
            throw new NebulaException(responseService);
        }
    }


}
