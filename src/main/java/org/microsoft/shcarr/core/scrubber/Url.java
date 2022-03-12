package org.microsoft.shcarr.core.scrubber;

import org.microsoft.shcarr.core.scrubber.PiiScrubber;

public class Url implements PiiScrubber {
    private static final String ACCESS_TOKEN_FILTERED = "access_token=[FILTERED]";
    private static final String SCRUBBED_PLACEHOLDER_FILENAME = "name=[NAME_SCRUBBED]";
    private static final String FILTERED_FILENAME_STRING= "FILTERED_FILENAME";

    // URL sample: /s3/files/59392900-0581-4708-bbab-ce990a45551b/1/here_is_filename.doc
    private static final String REQUEST_PATTERN = "(/s3/files/\\S*/\\d+/)(\\S*)";

    public Url() {
    }

    @Override
    public String scrubPii(final String input) {
        // TODO: as these expand, could potentially parse `input` as an uri, and operate on individual path and query components for better performance
        return scrubFilename(
                scrubAccessToken(input)
        );
    }

    // /some/path?name=this_is_my_filename.docx&other=param
    private static String scrubFilename(final String url) {
        String filteredUrl = url.replaceAll("name=([^&]+)", SCRUBBED_PLACEHOLDER_FILENAME);
        return filteredUrl.replaceAll(REQUEST_PATTERN, "$1" + FILTERED_FILENAME_STRING);
    }

    // https://word-view.officeapps.live.com/wv/ResReader.ashx?n=p2.img&v=00000000-0000-0000-0000-000000000802&usid=c8a396b5-4549-4591-b58b-b221bb1dfaf5&build=16.0.15102.41018&WOPIsrc=https%3A%2F%2Ffilesng%2Estaging%2Eyammer%2Ecom%2Fwopi%2Ffiles%2F207915106304&access_token=eyJhbGciOiJIUzI1NiJ9.eyJmaWxlLWlkIjoyMDc5MTUxMDYzMDQsInVzZXItaWQiOiJTaGFuZSBDYXJyIiwibG9jay1vYmplY3Qta2V5IjoiMTcxNDhcLzIwNzkxNTEwNjMwNFwvRW5nYWdlIFByb2R1Y3QgRGVmaW5pdGlvbi5kb2N4IiwiZWRpdC1lbmFibGVkIjpmYWxzZSwidGlkIjoiMTcxNDgiLCJuYmYiOjE2NDY5NDU5MjQsInN0b3JhZ2Uta2V5IjoiMTcxNDhcLzIwNzkxNTEwNjMwNFwvRW5nYWdlIFByb2R1Y3QgRGVmaW5pdGlvbi5kb2N4IiwiaG1hYyI6IjEwZWM0MDY1ZjBhOWMxYmM5MGE2OTU1ZTkzNDVhNTQzODkxNjhkMzFmOTU4YzkyNDM0MzdhMDI3NWNlNGIwZjgiLCJ2ZXJzaW9uLWlkIjoyMDg4NzU5Nzg3NTIsImZpbGUtc2l6ZSI6NjI3NTQ3MywiZmlsZS1uYW1lIjoiRW5nYWdlIFByb2R1Y3QgRGVmaW5pdGlvbi5kb2N4IiwiZXhwIjoxNjQ2OTUzMTI0LCJpYXQiOjE2NDY5NDU5ODQsImp0aSI6IjJkN2M4MzRhLTE2NmQtNDIyZi1iMmQxLTg1M2Q3ZDgyY2FkYyJ9.mRQhNPq0SB54WWO-H7NQM-F6m_AIstL0fO71zfRDjQI&access_token_ttl=0&z=208875978752%2Dda%2FXHnW2FVCotBxEMWGTMA%3D%3D&waccluster=PUS7
    private static String scrubAccessToken(final String url) {
        return url.replaceAll("access_token=([^&]+)", ACCESS_TOKEN_FILTERED);
    }
}
