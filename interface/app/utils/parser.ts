export const parseMoves = (movesString: string): string[] => {
    const trimmed = movesString.replace(/^\{|\}$/g, "");

    return trimmed.split(",").map(s => s.replace(/^"|"$/g, ""));
};
