def main() -> None:
    with open("testAll.txt", "wb") as file:
        barr = bytearray()
        
        for i in range(256):
            barr.extend([i]*(i+1))
        file.write(bytes(barr))
    with open("testHB.txt", "wb") as file:
        barr = bytearray()
        
        barr.extend([255]*(100))
        file.write(bytes(barr))

if __name__ == "__main__":
    main()