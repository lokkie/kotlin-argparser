// Copyright © 2016 Laurence Gonsalves
//
// This file is part of kotlin-optionparser, a library which can be found at
// http://github.com/xenomachina/kotlin-optionparser
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 2.1 of the License, or (at your
// option) any later version.
//
// This library is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
// for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this library; if not, see http://www.gnu.org/licenses/

package com.xenomachina.optionparser

import org.apache.commons.lang3.StringEscapeUtils
import kotlin.system.exitProcess

/**
 * An exception that wants the process to terminate with a specific status code, and also (optionally) wants to display
 * a message to [System.out] or [System.err].
 */
open class SystemExitException(val progName: String, message: String, val returnCode: Int) : Exception(message) {
    open fun printAndExit(): Nothing {
        System.err.println("$progName: $message")
        exitProcess(returnCode)
    }
}

/**
 * Indicates that an unrecognized option was supplied.
 */
open class UnrecognizedOptionException(progName: String, val optionName: String) :
        SystemExitException(progName, "unrecognized option '$optionName'", 2)

/**
 * Indicates that a value is missing after parsing has completed.
 */
open class MissingValueException(progName: String, val valueName: String) :
        SystemExitException(progName, "missing $valueName", 2)

/**
 * Indicates that the value of a supplied argument is invalid.
 */
open class InvalidArgumentException(progName: String, val argName: String, val argValue: String) :
        SystemExitException(progName, "invalid $argName: '${StringEscapeUtils.escapeJava(argValue)}'", 2)

/**
 * Indicates that a required argument was not supplied.
 */
open class OptionMissingRequiredArgumentException(progName: String, val optName: String) :
        SystemExitException(progName, "option '$optName' is missing a required argument", 2)

/**
 * Like [kotlin.run], but calls [SystemExitException.printAndExit] on any `SystemExitException` that is caught.
 */
fun <T, R> T.runMain(f: T.() -> R): R {
    try {
        return f()
    } catch (e: SystemExitException) {
        e.printAndExit()
    }
}
