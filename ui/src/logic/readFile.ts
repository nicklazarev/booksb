function readFile(file: File): Promise<string | ArrayBuffer | null> {
    const fileReader = new FileReader();
    return new Promise((resolve, reject) => {
        fileReader.onload = () => {
            resolve(fileReader.result);
        };

        fileReader.onerror = () => {
            reject(fileReader.error);
        };
        fileReader.readAsArrayBuffer(file);
    });
}

export default readFile;
