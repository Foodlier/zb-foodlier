import React, { useState } from 'react'

interface MoSearchProps {
  onSearch: (query: string) => void
}

const MoSearch: React.FC<MoSearchProps> = ({ onSearch }) => {
  const [query, setQuery] = useState('')

  const handleSearch = () => {
    onSearch(query)
  }

  return (
    <div>
      <input
        type="text"
        placeholder="검색어를 입력하세요"
        value={query}
        onChange={e => setQuery(e.target.value)}
      />
      <button type="button" onClick={handleSearch}>
        검색
      </button>
    </div>
  )
}

export default MoSearch
