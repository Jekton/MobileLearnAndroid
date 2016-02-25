package com.jekton.mobilelearn.course.file;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;

/**
 * @author Jekton
 */
interface FileActivityDocumentOps
        extends BasicDocumentOps<FileActivityOps>, FileDownloadService.DownloadObserver {

    void initFileList(String courseId);

    /**
     * Perform action according to the state of the file
     * @param courseFile CourseFile that user clicked
     */
    void performActionFor(CourseFile courseFile);

    void setDownloadingSetAvailable(boolean available);
}
