import { promises as fs } from "fs";

export default defineEventHandler(async event => {
    const config = useRuntimeConfig();
    const folderPath = config.dataDir as string;

    try {
        const files = await fs.readdir(folderPath);

        return { files };
    } catch {
        event.node.res.statusCode = 500;

        return { error: "InternalServerError" };
    }
});
