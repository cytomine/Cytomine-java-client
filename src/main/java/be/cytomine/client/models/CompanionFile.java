package be.cytomine.client.models;

public class CompanionFile extends Model<CompanionFile> {
    public CompanionFile(){}

    public CompanionFile(UploadedFile uploadedFile, AbstractImage image, String originalFilename, String filename, String type) {
        this(uploadedFile.getId(), image.getId(), originalFilename, filename, type);
    }

    public CompanionFile(Long uploadedFileId, Long imageId, String originalFilename, String filename, String type){
        this.set("uploadedFile", uploadedFileId);
        this.set("image", imageId);
        this.set("originalFilename",originalFilename);
        this.set("filename", filename);
        this.set("type", type);
    }
}
