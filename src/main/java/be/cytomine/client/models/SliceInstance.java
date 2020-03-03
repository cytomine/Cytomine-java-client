package be.cytomine.client.models;

public class SliceInstance extends Model<SliceInstance> {

    public SliceInstance() {}

    public SliceInstance(Project project, ImageInstance image, AbstractSlice baseSlice) {
        this(project.getId(), image.getId(), baseSlice.getId());
    }

    public SliceInstance(Long projectId, Long imageId, Long baseSliceId) {
        this.set("project", projectId);
        this.set("image", imageId);
        this.set("baseSlice", baseSliceId);
    }
}

