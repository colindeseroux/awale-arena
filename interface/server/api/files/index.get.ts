import { promises as fs } from "fs";
import path from "path";

export default defineEventHandler(async event => {
    const folderPath = path.join(process.cwd(), "data");

    try {
        const files = await fs.readdir(folderPath);

        return { files };
    } catch {
        event.node.res.statusCode = 500;

        return { error: "InternalServerError" };
    }
});
