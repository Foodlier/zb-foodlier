import React from 'react'
import SearchBar, { SearchResult } from './SearchBar' // SearchBar 컴포넌트와 SearchResult 타입을 가져옵니다.

interface WebSearchProps {
  onSearch: (results: SearchResult[]) => void
}

function WebSearch({ onSearch }: WebSearchProps) {
  return <SearchBar onSearch={onSearch} />
}

export default WebSearch
