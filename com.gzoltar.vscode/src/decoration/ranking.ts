/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
import { join, sep } from 'path';

export { Ranking, RankingGroup, RankingLine };

interface Ranking {
    [key: string]: RankingGroup;
}

class RankingGroup {
    public readonly low: RankingLine[] = [];
    public readonly medium: RankingLine[] = [];
    public readonly high: RankingLine[] = [];
    public readonly veryHigh: RankingLine[] = [];
    public readonly lines: RankingLine[] = [];
}

class RankingLine {

    public static readonly REGEX = /(.+)?\$(.+)\#(.+)\:(.+)\;(.+)/;

    private readonly name: string;
    private readonly line: number;
    private readonly suspiciousness: number;

    constructor(ranking: string) {
        const split = ranking.match(RankingLine.REGEX);

        if (split === null) {
            throw new Error('Inaccessible point.');
        }

        this.line = +split[split.length - 2] - 1;
        this.suspiciousness = +split[split.length - 1];
        this.name = split.length === 6
            ? join(split[1].replace(/\./g, sep), split[2])
            : split[1];
    }


    public getName(): string {
        return this.name;
    }


    public getLine(): number {
        return this.line;
    }


    public getSuspiciousness(): number {
        return this.suspiciousness;
    }
}
