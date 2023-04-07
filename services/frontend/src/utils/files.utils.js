const MB = 1024 * 1024;
export const MAX_FILE_SIZE = 1 * MB;

const signatures = {
    "UklGRm": "image/webp",
    "R0lGODdh": "image/gif",
    "R0lGODlh": "image/gif",
    "iVBORw0KGgo": "image/png",
    "/9j/": "image/jpg",
    "PD94b": "image/svg+xml",
    "AAAA": "image/avif"
};
  
export const detectMimeType =  (b64) => {
    for (var signature in signatures) {
        if (b64.indexOf(signature) === 0) {
            return signatures[signature];
        }
    }
    return "image/png";
}