import JSZip from "jszip";
import readFile from "./readFile";

interface EpubMetadata {
    isbn: string | null;
    cover: string | null;
}

async function parseEpubFile(epubFile: File): Promise<EpubMetadata> {
    const zipFile = await readFile(epubFile);
    const zip = new JSZip();
    const zipFileContent = await zip.loadAsync(zipFile as ArrayBuffer);
    let cover = null;
    try {
        const metadata = await zipFileContent.file("META-INF/container.xml").async("text");
        const domParser = new DOMParser();
        const doc = domParser.parseFromString(metadata, "application/xml");
        const contentPath = document.evaluate("//@full-path", doc, null, XPathResult.STRING_TYPE, null).stringValue;

        const contentFile = await zipFileContent.file(contentPath).async("text");
        const contentDoc = domParser.parseFromString(contentFile, "application/xml");
        const metaCoverItemId = document
            .evaluate('//*[name()="meta"][@name="cover"]/@content', contentDoc, null, XPathResult.STRING_TYPE, null)
            .stringValue;
        const coverPath = document
            .evaluate(`//*[name()="item"][@id="${metaCoverItemId}"]/@href`,
                contentDoc, null, XPathResult.STRING_TYPE, null)
            .stringValue;

        cover = await zipFileContent.file(coverPath).async("base64");
    } catch (e) {
        console.error(e);
    }

    return {
        cover,
        isbn: null,
    };
}

export default parseEpubFile;
