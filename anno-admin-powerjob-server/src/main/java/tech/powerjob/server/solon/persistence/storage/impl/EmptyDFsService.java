package tech.powerjob.server.solon.persistence.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.AppContext;
import tech.powerjob.server.solon.extension.dfs.DownloadRequest;
import tech.powerjob.server.solon.extension.dfs.FileLocation;
import tech.powerjob.server.solon.extension.dfs.FileMeta;
import tech.powerjob.server.solon.extension.dfs.StoreRequest;
import tech.powerjob.server.solon.persistence.storage.AbstractDFsService;

import java.io.IOException;
import java.util.Optional;

/**
 * EmptyDFsService
 *
 * @author tjq
 * @since 2023/7/30
 */
@Slf4j
public class EmptyDFsService extends AbstractDFsService {


    @Override
    public void store(StoreRequest storeRequest) throws IOException {
    }

    @Override
    public void download(DownloadRequest downloadRequest) throws IOException {
    }

    @Override
    public Optional<FileMeta> fetchFileMeta(FileLocation fileLocation) throws IOException {
        return Optional.empty();
    }

    @Override
    protected void init(AppContext context) {
        log.info("[EmptyDFsService] initialize successfully, THIS_WILL_BE_THE_STORAGE_LAYER.");
    }

}
