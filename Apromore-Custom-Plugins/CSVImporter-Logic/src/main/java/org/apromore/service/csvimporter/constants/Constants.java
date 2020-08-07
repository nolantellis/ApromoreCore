/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package org.apromore.service.csvimporter.constants;

public interface Constants {

    char[] supportedSeparators = {',','|',';','\t'};

    String possibleCaseId = "^((case)|(.*(case|service|event)(\\s|-|_)?(id)).*)$";
    String possibleActivity = "^((activity|operation)(.*)|event)$";
    String possibleResource = "^(resource|agent|employee|group)$";
    String possibleEndTimestamp = "^((.*(end|complete|completion).*)|(time:)?timestamp|date & time)$";
    String possibleStartTimestamp = "^(.*start.*)$";
    String possibleOtherTimestamp = "^(.*(date|time).*)$";
    String timestampPattern = "^(\\d{1,2}|\\d{4})([/\\-.])\\d{1,2}([/\\-.])(\\d{1,2}.*)$";


    String CSV_FILE_EXTENTION = "csv";
    String PARQUET_FILE_EXTENTION = "parquet";
}