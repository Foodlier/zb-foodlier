import React, { useState } from 'react'
import styled from 'styled-components'
import useIcon from '../../hooks/useIcon'
import { innerWidth, breakpoints, palette } from '../../constants/Styles'

interface WebSearchProps {
  onSearch: (query: string) => void
}

const SearchForm = styled.form`
  display: flex;
  align-items: center;
  position: relative;
  padding: 0 5%;

  ${breakpoints.large} {
    padding: 2% 0;
    ${innerWidth}
  }
`

const SearchInput = styled.input`
  flex: 1;
  outline: 0;
  border: 0.1rem solid var(--color-divider);
  border-radius: var(--size-border);
  padding: 1.5rem 2rem;
`

const SearchButton = styled.button`
  position: absolute;
  right: 0;
`

const WebSearch: React.FC<WebSearchProps> = () => {
  const { IcSearch } = useIcon()
  const [text, setText] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // navigate(`/videos/${text}`);
  }

  return (
    <SearchForm onSubmit={handleSubmit}>
      <SearchInput
        type="text"
        placeholder="재료, 제목, 요리사등으로 검색해보세요!"
        value={text}
        onChange={e => setText(e.target.value)}
      />
      <SearchButton type="button">
        <IcSearch size={3} color={palette.textPrimary} />
      </SearchButton>
    </SearchForm>
  )
}

export default WebSearch
