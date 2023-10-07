import React from 'react'
import SearchBar, { SearchResult } from './SearchBar'

interface WebSearchProps {
  onSearch: (results: SearchResult[]) => void
}

function WebSearch({ onSearch }: WebSearchProps) {
  return <SearchBar onSearch={onSearch} />
}

export default WebSearch
