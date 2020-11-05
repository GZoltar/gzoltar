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
