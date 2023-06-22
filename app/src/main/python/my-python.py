from pyserini.search.lucene import LuceneSearcher
import json

class python_searcher:
    def __init__(self):
        self.searcher = LuceneSearcher.from_prebuilt_index('msmarco-v1-passage')
    def search(self, query, k=10):
        hits = self.searcher.search(query, k=k)
        return hits
    def print_search(self, query, k=10):
        hits = self.searcher.search(query, k=10)
        for i in hits:
            print(json.loads(i.raw)['contents'])