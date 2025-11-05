import { promises as fs } from "fs";
import path from "path";

export default defineEventHandler(async event => {
    const { filename } = event.context.params as { filename: string };

    const folderPath = path.join(process.cwd(), "data");
    const safePath = path.join(folderPath, filename);

    if (!safePath.startsWith(folderPath)) {
        event.node.res.statusCode = 400;

        return { error: "InvalidFilePath" };
    }

    try {
        const content = await fs.readFile(safePath, "utf-8");

        return { filename, content };
    } catch (err) {
        event.node.res.statusCode = 404;

        return { error: "FileNotFound" };
    }
});
