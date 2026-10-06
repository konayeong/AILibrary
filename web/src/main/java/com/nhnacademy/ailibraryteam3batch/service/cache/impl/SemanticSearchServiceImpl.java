package com.nhnacademy.ailibraryteam3batch.service.cache.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.service.book.EmbeddingService;
import com.nhnacademy.ailibraryteam3batch.service.cache.SemanticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SemanticSearchServiceImpl implements SemanticSearchService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmbeddingService embeddingService;
    private final double THRESHOLD = 0.05;

    @Override
    public List<BookSearchResponse> cacheVectorSearch(String context) {

        float[] embeddedContext = embeddingService.getEmbedding(context);

        try (Socket socket = new Socket("220.67.216.14", 6379)) {

            socket.setSoTimeout(5000);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            byte[] vec = floatArrayToBytes(embeddedContext);

            //쿼리 바이트로 생성
            List<byte[]> args = List.of(
                    "FT.SEARCH".getBytes(),
                    "team3_semantic_cache_index".getBytes(),
                    "*=>[KNN 10 @embedding $vec AS score]".getBytes(),

                    "PARAMS".getBytes(),
                    "2".getBytes(),
                    "vec".getBytes(),
                    vec,

                    "RETURN".getBytes(),
                    "4".getBytes(),
                    "$.question".getBytes(),
                    "$.searchType".getBytes(),
                    "$.books".getBytes(),
                    "score".getBytes(),

                    "SORTBY".getBytes(),
                    "score".getBytes(),
                    "ASC".getBytes(),

                    "DIALECT".getBytes(),
                    "2".getBytes()
            );

            //먼저 인증
            sendResp(out, List.of(
                    "AUTH".getBytes(),
                    "*N2vya7H@muDTwdNMR!".getBytes()
            ));
            Object auth = readResp(in); // ok 응답

            //인덱스를 이용한 코사인 검색 샐시
            sendResp(out, args);
            Object result = readResp(in);

            List<?> res = (List<?>) result;

            long total = (Long) res.get(0);

            if (total == 0) {
                return List.of();
            }

            List<?> fields = (List<?>) res.get(2);

            Map<String,Object> map = new HashMap<>();

            for (int i = 0; i < fields.size(); i += 2) {
                map.put(fields.get(i).toString(), fields.get(i + 1));
            }

            double score =
                    Double.parseDouble(map.get("score").toString());
            // 임계값보다 높으면 버림
            if (score > THRESHOLD) {
                return List.of();
            }

            Object booksObj = map.get("$.books");
            if (booksObj == null) {
                return List.of();
            }

            return objectMapper.readValue(
                    booksObj.toString(),
                    new TypeReference<List<BookSearchResponse>>() {});


        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static byte[] floatArrayToBytes(float[] vector) {
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES * vector.length)
                .order(ByteOrder.LITTLE_ENDIAN);

        for (float v : vector) {
            buffer.putFloat(v);
        }

        return buffer.array();
    }

    //redis용으로 작성 및 전송
    private void sendResp(OutputStream out, List<byte[]> args) throws IOException {

        out.write(("*" + args.size() + "\r\n").getBytes());

        for (byte[] arg : args) {

            out.write(("$" + arg.length + "\r\n").getBytes());
            out.write(arg);
            out.write("\r\n".getBytes());
        }

        out.flush();
    }

    //바이트 데이터 읽기
    private Object readResp(InputStream in) throws IOException {

        int prefix = in.read();

        switch (prefix) {

            case '+':
                return readLine(in);

            case ':':
                return Long.parseLong(readLine(in));

            case '$': {
                int len = Integer.parseInt(readLine(in));

                if (len == -1) {
                    return null;
                }

                byte[] data = in.readNBytes(len);

                in.read();
                in.read();

                return new String(data);
            }

            case '*': {

                int size = Integer.parseInt(readLine(in));

                List<Object> list = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    list.add(readResp(in));
                }

                return list;
            }

            case '-':
                throw new RuntimeException(readLine(in));

            default:
                throw new RuntimeException("Unknown RESP : " + (char) prefix);
        }
    }

    //한줄 읽기
    private String readLine(InputStream in) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while (true) {

            int b = in.read();

            if (b == '\r') {

                in.read();

                break;
            }

            bos.write(b);
        }

        return bos.toString();
    }
}
