import struct

def decode_opus_from_file(input_file):
    """
    p3 Opus ， Opus 。
    """
    opus_datas = []
    total_frames = 0
    sample_rate = 16000
    frame_duration_ms = 60
    frame_size = int(sample_rate * frame_duration_ms / 1000)

    with open(input_file, 'rb') as f:
        while True:

            header = f.read(4)
            if not header:
                break


            _, _, data_len = struct.unpack('>BBH', header)


            opus_data = f.read(data_len)
            if len(opus_data) != data_len:
                raise ValueError(f"Data length({len(opus_data)}) mismatch({data_len}) in the file.")

            opus_datas.append(opus_data)
            total_frames += 1


    total_duration = (total_frames * frame_duration_ms) / 1000.0
    return opus_datas, total_duration

def decode_opus_from_bytes(input_bytes):
    """
    p3 Opus ， Opus 。
    """
    import io
    opus_datas = []
    total_frames = 0
    sample_rate = 16000
    frame_duration_ms = 60
    frame_size = int(sample_rate * frame_duration_ms / 1000)

    f = io.BytesIO(input_bytes)
    while True:
        header = f.read(4)
        if not header:
            break
        _, _, data_len = struct.unpack('>BBH', header)
        opus_data = f.read(data_len)
        if len(opus_data) != data_len:
            raise ValueError(f"Data length({len(opus_data)}) mismatch({data_len}) in the bytes.")
        opus_datas.append(opus_data)
        total_frames += 1

    total_duration = (total_frames * frame_duration_ms) / 1000.0
    return opus_datas, total_duration