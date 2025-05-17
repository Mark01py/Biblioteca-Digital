import cv2
import numpy as np
import os
from pathlib import Path

# Caminho da imagem original (garante compatibilidade com Windows)
base_path = Path(__file__).resolve().parent.parent / "src" / "main" / "resources" / "public"
caminho_imagem = base_path / "imagem.jpg"
saida_imagem = base_path / "static" / "superpixel_resultado.jpg"

# Carrega a imagem
imagem = cv2.imread(str(caminho_imagem))
if imagem is None:
    print(f"Erro: Imagem não encontrada em {caminho_imagem}")
    exit()

imagem = cv2.resize(imagem, (400, 300))
imagem_float = np.float32(imagem) / 255.0

# Aplica SLIC
slic = cv2.ximgproc.createSuperpixelSLIC(imagem_float, algorithm=cv2.ximgproc.SLIC, region_size=20, ruler=20)
slic.iterate(10)

mask = slic.getLabelContourMask()
output = imagem.copy()
output[mask == 255] = (0, 0, 255)

# Cria a pasta se necessário e salva a imagem processada
saida_imagem.parent.mkdir(parents=True, exist_ok=True)
cv2.imwrite(str(saida_imagem), output)
print("✅ Imagem com superpixels gerada em:", saida_imagem)
