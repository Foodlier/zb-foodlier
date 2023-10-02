import React from 'react'
import SearchBar from './SearchBar'

interface WebSearchProps {
  onSearch: (query: string) => void
}

function WebSearch({ onSearch }: WebSearchProps) {
  const handleSearch = (query: string) => {
    onSearch(query)
  }

  return <SearchBar onSearch={handleSearch} />
}

export default WebSearch
