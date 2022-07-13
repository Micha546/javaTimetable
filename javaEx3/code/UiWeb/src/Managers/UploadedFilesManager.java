package Managers;

import InformationHolders.FileInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UploadedFilesManager {

    private final List<FileInfo> fileInfoArrayList = new ArrayList<>();

    public synchronized void addFileInfo(FileInfo uploadedFileInfo)
    {
        fileInfoArrayList.add(uploadedFileInfo);
    }

    public synchronized List<FileInfo> getFileInfoList()
    {
        return new ArrayList<>(fileInfoArrayList);
    }

    public synchronized FileInfo getFileInfoByID(int id)
    {
        return fileInfoArrayList.stream()
                .filter(fileInfo -> fileInfo.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
